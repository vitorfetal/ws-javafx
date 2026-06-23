package com.prisma.java_fx;

import java.sql.ResultSet;
import db.DB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class JavaFxApplication {

	public static void main(String[] args) throws SQLException {

		SpringApplication.run(JavaFxApplication.class, args);

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try
		{
			conn = DB.getConnection();

			st = conn.createStatement();

			rs = st.executeQuery("select * from department");

			while (rs.next())
			{
				System.out.println(rs.getInt("Id") + ", " + rs.getString("Name"));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
			DB.closeConnection(conn);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		Connection conn2 = null;
		PreparedStatement st2 = null;
		rs = null;
		try {
			conn2 = DB.getConnection();

			st2 = conn2.prepareStatement(
							"INSERT INTO seller " +
							"(Name, Email, BirthDate, BaseSalary, DepartmentId)" +
									"VALUES" +
									"(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st2.setString(1, "Carl Purple");
			st2.setString(2, "Carl@gmail.com");
			st2.setDate(3, new Date(sdf.parse("22/04/1985").getTime()));
			st2.setDouble(4, 3000.00);
			st2.setInt(5, 4);


			int rowsAffected = st2.executeUpdate();
			if (rowsAffected > 0 )
			{
				System.out.println("Done! Rows affected: " + rowsAffected);
				rs = st2.getGeneratedKeys();
				while (rs.next())
				{
					int id = rs.getInt(1);
					System.out.println("Done! Id = " +  id);
				}
			}
				System.out.println("Not affected new rows");



		} catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
		finally {
			DB.closeStatement(st2);
			DB.closeResultSet(rs);
			DB.closeConnection(conn2);

		}

    }

}
