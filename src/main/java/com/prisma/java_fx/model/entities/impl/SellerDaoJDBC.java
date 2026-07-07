package com.prisma.java_fx.model.entities.impl;

import com.prisma.java_fx.model.entities.Department;
import com.prisma.java_fx.model.entities.Seller;
import com.prisma.java_fx.model.entities.dao.SellerDao;
import db.DB;
import db.exceptions.DbException;
import org.hibernate.annotations.processing.SQL;
import org.hibernate.id.CompositeNestedGeneratedValueGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao
{

    private Connection conn = null;

    public SellerDaoJDBC(Connection conn){this.conn = conn;}

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;

        try

        {
            st = conn.prepareStatement(
                    "INSERT INTO seller "
                      +  "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                      +  "VALUES "
                      +  "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

           int rowsAffected = st.executeUpdate();

           if (rowsAffected > 0)
           {
               ResultSet rs = st.getGeneratedKeys();
                if (rs.next())
                {
                    int id = rs.getInt(1);
                    seller.setId(id);
                }

                DB.closeResultSet(rs);
           }

           else
               {
                  throw new DbException("Unexpectd error! No rows affected!");
               }

        }

        catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }

        finally {
            DB.closeStatement(st);
        }


    }

    @Override
    public void update(Seller seller) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement st = null;

        try
        {
            st = conn.prepareStatement("UPDATE seller "
                                          + "SET Name = ?, "
                                          + "Email = ?, "
                                          + "BirthDate = ?, "
                                          + "BaseSalary = ?, "
                                          + "DepartmentId = ? "
                                          + "WHERE id = ? "
            );

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary() + 3000.00);
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

        }

        catch (SQLException e)
        {
            throw new DbException(e.getMessage());

        }
        finally
        {
            DB.closeResultSet(rs);
            DB.closeStatement(st);

        }


    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;


        try
            {

                st = conn.prepareStatement("DELETE FROM seller WHERE Id = ? ");

                st.setInt(1, id);

                int rows = st.executeUpdate();

                if (rows == 0)
                {
                    throw new DbException("Id não exite");
                }

            }
        catch (SQLException e)
            {
                throw new DbException(e.getMessage());
            }
        finally {
            DB.getConnection();
        }





    }

    @Override
    public Seller findById(Integer id) throws SQLException {
        Connection conn = DB.getConnection();
        ResultSet rs = null;

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName"
                            + "FROM seller INNER JOIN department"
                            + "ON seller.DepartmentId = department.id"
                            + "WHERE seller.Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Department dep = instanceDepartment(rs);

                return instanceSeller(rs, dep);
            }

            return null;
        }

        catch (SQLException e) {throw new SQLException(e.getMessage());}

        finally {DB.closeStatement(st); DB.closeResultSet(rs);}


    }

    private Seller instanceSeller(ResultSet rs, Department dep) throws SQLException
    {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setDepartment(dep);

        return seller;
    }

    private Department instanceDepartment(ResultSet rs) throws SQLException
    {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("Name"));

        return dep;
    }

    @Override
    public List<Seller> findAll() throws SQLException {
        Connection conn = DB.getConnection();
        ResultSet rs = null;

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name ");

            rs = st.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId")); //Validação da existência do Department por ID

                if (dep == null){dep = instanceDepartment(rs);}

                Seller seller = instanceSeller(rs, dep);

                sellers.add(seller);

            }


            return sellers;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        Connection conn = DB.getConnection();
        ResultSet rs = null;

        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ?" +
                            "ORDER BY Name ");

            st.setInt(1, 1);

            rs = st.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId")); //Validação da existência do Department pore ID

                if (dep == null){dep = instanceDepartment(rs);}

                Seller seller = instanceSeller(rs, dep);

                sellers.add(seller);

            }


            return sellers;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

}
