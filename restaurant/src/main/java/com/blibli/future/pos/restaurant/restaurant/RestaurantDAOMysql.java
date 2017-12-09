package com.blibli.future.pos.restaurant.restaurant;

import com.blibli.future.pos.restaurant.common.MysqlDAO;
import com.blibli.future.pos.restaurant.common.model.Restaurant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class RestaurantDAOMysql extends MysqlDAO implements RestaurantDAO {

    private void setRestaurantFromQuery(Restaurant restaurant, ResultSet rs) throws SQLException {
        restaurant.setId(rs.getInt("id"));
        restaurant.setTimestampCreated(rs.getTimestamp("timestamp_created"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setPhone(rs.getString("phone"));
        restaurant.autoSetHref();
    }

    @Override
    public boolean create(Restaurant restaurant) {
        try {
            String query = "INSERT INTO restaurants(address, phone)" +
                    " VALUES(?, ?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, restaurant.getAddress());
            ps.setString(2, restaurant.getPhone());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No restaurant inserting");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on create restaurant");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Restaurant getById(int id) {
        Restaurant restaurant = new Restaurant();
        if (!open()) {
            return restaurant;
        }
        try {
            String query = "SELECT * FROM restaurants WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            rs.next();
            setRestaurantFromQuery(restaurant, rs);
            return restaurant;
        } catch (SQLException e) {
            message.setMessage("Something wrong on get restaurant");
            e.printStackTrace();
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> getBulk(String filter) {
        List<Restaurant> restaurants = new ArrayList<>();
        if (!open()) {
            return restaurants;
        }
        try {
            String query = "SELECT * FROM restaurants WHERE "+filter;
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Restaurant restaurant= new Restaurant();
                setRestaurantFromQuery(restaurant, rs);
                restaurants.add(restaurant);
            }
            return restaurants;
        } catch (SQLException e) {
            message.setMessage("Something wrong on getBulk restaurants");
            e.printStackTrace();
        }
        return restaurants;
    }

    @Override
    public boolean delete(int id) {

        try {
            String query = "DELETE FROM restaurants WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No restaurant deleted");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on delete restaurant");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int id, Restaurant restaurant) {

        try {
            String query = "UPDATE categories SET id = ?, address = ?, phone = ? WHERE id = ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, restaurant.getAddress());
            ps.setString(2, restaurant.getPhone());
            ps.setInt(3, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                return true;
            } else {
                message.setMessage("No affected query. No restaurant update");
            }
        } catch (SQLException e) {
            message.setMessage("Something wrong on update restaurant");
            e.printStackTrace();
        }
        return false;
    }
}
