package com.prisma.java_fx.model.entities.dao;

import com.prisma.java_fx.model.entities.impl.SellerDaoJDBC;
import db.DB;

public class DaoFactory
{

    public static SellerDao createSellerDao()
    {
        return new SellerDaoJDBC(DB.getConnection());
    }

}
