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
import java.util.List;

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
            } else {
                return null;
            }


        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);

        }


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

}
