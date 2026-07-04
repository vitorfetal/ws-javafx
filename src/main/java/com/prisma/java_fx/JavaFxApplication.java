package com.prisma.java_fx;

import com.prisma.java_fx.model.entities.Seller;
import com.prisma.java_fx.model.entities.dao.DaoFactory;
import com.prisma.java_fx.model.entities.dao.SellerDao;
import db.DB;
import db.exceptions.DbException;
import db.exceptions.DbFakeError;
import db.exceptions.DbIntegrityException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.Scanner;

@SpringBootApplication
public class JavaFxApplication {

	public static void main(String[] args)  {

		SpringApplication.run(JavaFxApplication.class, args);


		SellerDao sellerDao = DaoFactory.createSellerDao();

		try {

			Seller sellerOne = sellerDao.findById(3);

			System.out.println(sellerOne);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}






	}

}
