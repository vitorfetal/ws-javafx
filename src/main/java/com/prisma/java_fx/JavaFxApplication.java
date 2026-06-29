package com.prisma.java_fx;

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

	public static void main(String[] args) throws SQLException {

		SpringApplication.run(JavaFxApplication.class, args);

//		//Abrindo a conexão
//
//		Connection conn = null;
//		Statement st = null;
//		ResultSet rs = null;
//
//		try
//		{
//			conn = DB.getConnection();
//
//			st = conn.createStatement();
//
//			rs = st.executeQuery("select * from department");
//
//			while (rs.next())
//			{
//				System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
//			}
//		}
//		catch (SQLException e)
//		{
//			e.printStackTrace();
//		}
//		finally {
//			DB.closeResultSet(rs);
//			DB.closeStatement(st);
//			DB.closeConnection(conn);
//		}
//
//
//
//		//Realizando um Load para o banco do fluxo ETL
//
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
//		Connection conn2 = null;
//		PreparedStatement st2 = null;
//		rs = null;
//		try {
//			conn2 = DB.getConnection();
//
//			st2 = conn2.prepareStatement(
//							"INSERT INTO seller " +
//							"(Name, Email, BirthDate, BaseSalary, DepartmentId)" +
//									"VALUES" +
//									"(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
//			st2.setString(1, "Carl Purple");
//			st2.setString(2, "Carl@gmail.com");
//			st2.setDate(3, new Date(sdf.parse("22/04/1985").getTime()));
//			st2.setDouble(4, 3000.00);
//			st2.setInt(5, 4);
//
//
//			int rowsAffected = st2.executeUpdate();
//			if (rowsAffected > 0 )
//			{
//				System.out.println("Done! Rows affected: " + rowsAffected);
//				rs = st2.getGeneratedKeys();
//				while (rs.next())
//				{
//					int id = rs.getInt(1);
//					System.out.println("Done! Id = " +  id);
//				}
//			}
//				System.out.println("Not affected new rows");
//
//
//
//		} catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//		finally {
//			DB.closeStatement(st2);
//			DB.closeResultSet(rs);
//			DB.closeConnection(conn2);
//
//		}


//		 Realizando o fluxo de Update do fluxo de ETL
//		Connection conn3 = null;
//		PreparedStatement statement2 = null;
//		Scanner sc = new Scanner(System.in);
//
//		try{
//
//			conn3 = DB.getConnection();
//			statement2 = conn3.prepareStatement("UPDATE seller "
//													+ "SET BaseSalary = BaseSalary + ? "
//													+ "WHERE "
//													+ "(DepartmentId = ?)");
//
//			statement2.setDouble(1, sc.nextDouble());
//			statement2.setInt(2, sc.nextInt());
//
//			int informateAffectorRowsTwo = statement2.executeUpdate();
//			System.out.println("Done! " + informateAffectorRowsTwo);
//
//		} catch (SQLException e)
//		{
//			e.printStackTrace();
//		}
//		finally {
//			DB.closeStatement(statement2);
//			DB.closeConnection(conn3);
//		}

//		Realizando o fluxo de Deleção com ETL + Criação de exceção personalizada
//		Connection conn3 = null;
//		PreparedStatement statement2 = null;
		Scanner sc = new Scanner(System.in);
//
//		try{
//
//			conn3 = DB.getConnection();
//			statement2 = conn3.prepareStatement("DELETE FROM DepartmentId "
//													+ "	WHERE) "
//													+ "Id = ? ");
//
//
//
//
//			statement2.setInt(1, sc.nextInt());
//			sc.nextLine();
//
//			int informateAffectorRowsTwo = statement2.executeUpdate();
//			System.out.println("Done! " + informateAffectorRowsTwo);
//
//		} catch (SQLException e)
//		{
//			throw new DbIntegrityException(e.getMessage());
//		}
//		finally {
//			DB.closeStatement(statement2);
//			DB.closeConnection(conn3);
//		}

		Connection conn = null;
		Statement statement = null;

		try {

			conn = DB.getConnection();

			conn.setAutoCommit(false); //Em caso de erro, não haverá confirmação de transação automatica = false

			statement = conn.createStatement();

			int rows1 = statement.executeUpdate("UPDATE seller SET BaseSalary = 3000 WHERE DepartmentId = 2 ");

			int x = 1;
			if (x < 2)
			{

			throw  new DbFakeError("Erro fake");

			}

			int rows2 = statement.executeUpdate("UPDATE seller SET BaseSalary = BaseSalary + 3000 WHERE DepartmentId = 3 ");

			conn.commit(); //Caso não ocorra erro, será feito o processo de confirmação da transação

		} catch (SQLException e)
		{
			try{
				conn.rollback(); //Em caso de erro, ocorrerá o tratamento para retorno do estado antigo de transação (ACID)

				throw new DbException("Transection rolled back! Caused by: " + e.getMessage());

			} catch (SQLException e1)
			{
				throw  new DbException("Error trying to rolback! Caused by: " + e1.getMessage());
			}

        }

		finally {
			DB.closeStatement(statement);
			DB.closeConnection(conn);
		}

    }

}
