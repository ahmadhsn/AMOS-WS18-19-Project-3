package com.gr03.amos.bikerapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        try {
//            PostgreSQLExample postgreSQLExample = new PostgreSQLExample();
//            Connection conn = postgreSQLExample.getPostgreSQLConnection();
////            PreparedStatement st = conn.prepareStatement("INSERT INTO STUDENT (NAME) VALUES (?)");
////            st.setString(1, "ahsan");
////            st.executeUpdate();
//
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM STUDENT;");
//
//            while ( rs.next() ) {
//                String  name = rs.getString("name");
//                System.out.println( "NAME = " + name );
//            }
//            stmt.close();
////            conn.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        setContentView(R.layout.activity_login);
    }
}