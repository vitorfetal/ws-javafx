package com.prisma.java_fx.model.entities.impl;

import com.prisma.java_fx.model.entities.Department;
import com.prisma.java_fx.model.entities.Seller;
import com.prisma.java_fx.model.entities.dao.SellerDao;
import db.DB;
import org.hibernate.annotations.processing.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao
{

    private Connection conn;

    public SellerDaoJDBC(Connection conn){this.conn = conn;}

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {

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
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findByDepartment(Department department) throws SQLException {
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

                Department dep = map.get(rs.getInt("DepartmentId")); //Validação da existencia do Department pore ID

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

}
