package com.prisma.java_fx.model.entities.dao;

import com.prisma.java_fx.model.entities.Seller;

import java.sql.SQLException;
import java.util.List;

public interface SellerDao 
{
    void insert (Seller seller);
    void update (Seller seller);
    void deleteById (Integer id);
    Seller findById (Integer id) throws SQLException;
    List<Seller> findAll();

}
