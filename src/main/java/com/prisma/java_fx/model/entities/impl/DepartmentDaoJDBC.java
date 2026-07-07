package com.prisma.java_fx.model.entities.impl;

import com.prisma.java_fx.model.entities.Department;
import com.prisma.java_fx.model.entities.dao.DepartmentDao;
import db.DB;
import db.exceptions.DbException;
import org.hibernate.annotations.processing.SQL;

import javax.naming.ldap.PagedResultsControl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao
{

    public Connection conn = null;

    public DepartmentDaoJDBC (Connection conn){this.conn = conn;}


    @Override
    public void insert(Department department) {

        try
            {

            PreparedStatement st = conn.prepareStatement("INSERT INTO department Name VALUES ? ");

              st.setString(1, department.getName());

              int rows = st.executeUpdate();

              if (rows > 0)
              {
                  ResultSet rs = st.getGeneratedKeys();

                  if (rs.next())
                  {
                      int id = rs.getInt(1);
                      department.setId(id);
                  }

                  DB.closeResultSet(rs);

              }
              else {throw new DbException("Not rows affected");}

        }
        catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }

    }

    @Override
    public void update(Department department) {
         PreparedStatement st = null;
         ResultSet rs = null;
        try {
            st = conn.prepareStatement("UPDATE department "
                                         + "SET Name = ? ");


            st.setString(1, department.getName());

            int rows = st.executeUpdate();

            if (rows > 0)
            {
                throw new DbException("Update sucess");
            }
            else
            {
                throw new DbException("Update fail");
            }
        }
        catch (SQLException e)
        {
            throw new DbException(e.getMessage());
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

            if (rows > 0)
            {
                throw new DbException("Delete sucess bry Id");
            }

        }
        catch (SQLException e)
        {
            throw new DbException("Error to delete by Id");
        }

    }

    private Department instanceDepartment(ResultSet rs)
    {
        try{

        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("Name"));

        return dep;

        }

        catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }
    }


    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try
        {
            st = conn.prepareStatement("SELECT * department WHERE id = ?");

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next())
            {
                Department dep = instanceDepartment(rs);

                return dep;

            }
            else
            {
                return null;
            }



        }
        catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }


    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet set = null;

        try
        {
            st = conn.prepareStatement("SELECT deparment.*,department.Name as DepName "
                                          + "FROM seller ");

            set = st.executeQuery();

            List<Department> dep = new ArrayList<>();
            Map<Integer, Department> depMap = new HashMap<>();

            while (set.next())
            {
                Department depTwo = depMap.get(set.getInt("Id"));

                if (depTwo == null)
                {
                    depTwo = instanceDepartment(set);

                }

                Department depTree = instanceDepartment(set);
                dep.add(depTree);

            }
            return dep;

        }
        catch (SQLException e)
        {
            throw new DbException(e.getMessage());
        }

    }


}
