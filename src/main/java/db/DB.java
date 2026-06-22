package db;

import org.hibernate.annotations.processing.SQL;

import java.sql.*;

public class DB
{
    private static final String URL = "jdbc:mysql://localhost:3306/coursejdbc";

    private static final String USER = "root";
    private static final String PASSWORD = "0852";

    public static Connection getConnection()
        {
            try {

                return DriverManager.getConnection(
                        URL,
                        USER,
                        PASSWORD
                );
            }
                catch (SQLException e)
                {
                    throw new DbException(e.getMessage());
                }


            }

            public static void closeConnection(Connection conn)
            {
                if (conn != null)
                {
                    try {
                        conn.close();
                    }
                    catch (SQLException e)
                    {
                        throw new DbException(e.getMessage());
                    }
                }
            }

            public static void closeStatement(Statement st)
            {
                if (st != null)
                {
                    try {
                        st.close();
                    } catch (SQLException e)
                    {
                        throw new DbException(e.getMessage());
                    }
                }
            }

            public static void closeResultSet(ResultSet rs)
            {
                if(rs != null)
                {
                    try
                    {
                        rs.close();
                    } catch (SQLException e)
                    {
                        throw new DbException(e.getMessage());
                    }
                }
            }




}
