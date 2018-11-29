package com.jwt.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jwt.DataBaseConnection.DatabaseProvider;
import com.jwt.model.Address;
import com.jwt.model.Route;

public class RouteDaoImplementation implements RouteDao {

 public void createRoute(Address startAddress, Address endAddress, Route route) {
  try {
   AddressDao startAddressDao = new AddressDaoImplementation();
   AddressDao endAddressDao = new AddressDaoImplementation();
   startAddressDao.createAddress(startAddress);
   endAddressDao.createAddress(endAddress);
   createRoute(route, startAddress.getId(), endAddress.getId());
  } catch (Exception ex) {
   ex.printStackTrace();
  }
 };

 void createRoute(Route route, int startAddressId, int endAddressId) throws SQLException {
  DatabaseProvider provider = DatabaseProvider.getInstance();
  String sqlQuerry = "INSERT INTO route(id_user, name, description, startpoint, endpoint)VALUES (?, ?, ?, ?, ?);";
  PreparedStatement st = provider.getConnection().prepareStatement(sqlQuerry, Statement.RETURN_GENERATED_KEYS);
  st.setInt(1, route.getUser_id());
  st.setString(2, route.getName());
  st.setString(3, route.getDescription());
  st.setInt(4, startAddressId);
  st.setInt(5, endAddressId);

  int affectedRows = st.executeUpdate();

  if (affectedRows == 0) {
   throw new SQLException("Creating user failed, no rows affected.");
  }

  try (ResultSet generatedKeys = st.getGeneratedKeys()) {
   if (generatedKeys.next()) {
    route.setId(generatedKeys.getInt(1));
   } else {
    throw new SQLException("Creating user failed, no ID obtained.");
   }
  }
  st.closeOnCompletion();
 }

 public void updateRoute(Route route) {
  //		TODO: Implement update route
 };
 public void removeRoute(Route route) {
  //		TODO: Implement delete route
 };
}