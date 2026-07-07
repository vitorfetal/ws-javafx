package com.prisma.java_fx.model.entities.dao;

import com.prisma.java_fx.model.entities.Department;
import com.prisma.java_fx.model.entities.impl.DepartmentDaoJDBC;
import com.prisma.java_fx.model.entities.impl.SellerDaoJDBC;
import db.DB;

public class DaoFactory
{

    public static SellerDao createSellerDao() {return new SellerDaoJDBC(DB.getConnection());}

    public static DepartmentDao createDepartmentDao() {return new DepartmentDaoJDBC(DB.getConnection());}

}
