package com.tripco.t11.TIP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TIPFind extends TIPHeader {
    //db info
    private final static String myDriver = "com.mysql.jdbc.Driver";
    private final static String myUrl = "jdbc:mysql://faure.cs.colostate.edu/cs314";
    private final static String user = "cs314-db";
    private final static String pass = "eiK5liet1uej";
    //class vars
    private String match;
    private Integer limit;
    private Integer found;
    private Map<String, Object>[] places;
    private Map<String, Object>[] narrow;
    //logger
    private final transient Logger log = LoggerFactory.getLogger(TIPFind.class);

    public TIPFind(String match, int limit, Map<String, Object>[] narrow){
        this();
        this.match = match;
        this.limit = limit;
        this.narrow = getNarrow(narrow);
    }

    private TIPFind(){
        this.requestType = "find"; 6
        this.requestVersion = 4;
    }

    private Map<String,Object>[] getNarrow(Map<String,Object>[] filters){
        if(filters != null){
            TIPConfig config = new TIPConfig();
            config.buildResponse();
            return config.getFilters();
        }
        return filters;
    }

    @Override
    public void buildResponse(){
        try {
            Class.forName(myDriver);
            try (Connection connect = DriverManager.getConnection(myUrl, user, pass);
                 Statement stCount = connect.createStatement();
                 Statement stQuery = connect.createStatement();
                 ResultSet rsCount = stCount.executeQuery(buildCountQuery());
                 ResultSet rsQuery = stQuery.executeQuery(buildMatchQuery(getPlaceAttributes()));
            )   {
                rsCount.next();
                this.found = rsCount.getInt(1);
                addPlaces(rsQuery, getPlaceAttributes());
            }
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
        }
        log.trace("buildResponse -> {}", this);
    }

    private static String[] getPlaceAttributes(){
        String[] placeAttributes = new String[] {"world.name", "world.municipality", "world.latitude", "world.longitude",
                "region.name", "country.name", "continent.name"};
        return placeAttributes;
    }

    private String buildCountQuery(){
        String countQuery = "select count(*) " + queryEnd() + ";";
        return countQuery;
    }

    private String buildMatchQuery(String[] placeAttributes){
        String matchQuery = "SELECT ";
        for(int i = 0; i < placeAttributes.length - 1; ++i){
            matchQuery += placeAttributes[i] + ", ";
        }
        matchQuery += placeAttributes[placeAttributes.length - 1] + " ";
        matchQuery += queryEnd();
        if(limit != null){
            if(limit != 0){
                matchQuery += " limit " + limit;
            }
        }
        matchQuery += ";";
        return matchQuery;
    }

    private String queryEnd(){
        String queryEnd = "FROM continent ";
        queryEnd += concatMapJoin() + concatMatchSearch();
        if(narrow != null) queryEnd += concatFilterSearch();
        return queryEnd;
    }

    private String concatMapJoin(){
        String mapJoin = "INNER JOIN country ON continent.id = country.continent "
                + "INNER JOIN region ON country.id = region.iso_country "
                + "INNER JOIN world ON region.id = world.iso_region ";
        return mapJoin;
    }

    private String concatMatchSearch(){
        String matchSearch = "WHERE country.name LIKE" + getSearchString(this.match)
                + "OR region.name LIKE" + getSearchString(this.match)
                + "OR world.name LIKE" + getSearchString(this.match)
                + "OR world.municipality LIKE" + getSearchString(this.match);
        return matchSearch;
    }

    private String getSearchString(String search){
        return " \"%" + search + "%\" ";
    }

    private String concatFilterSearch(){
        String filterSearch = "";
        for(int i = 0; i < narrow.length; ++i){
            if ( ((String)narrow[i].get("name")).equals("ports") ) {
                ArrayList<String> filters = ((ArrayList<String>)narrow[i].get("values"));
                filterSearch += extractSearchStrings(filters);
            }
        }
        return filterSearch;
    }

    private String extractSearchStrings(ArrayList<String> filters){
        String filterSearch = "";
        for(int j = 0; j < filters.size(); ++j){
            filterSearch += "AND world.type LIKE"
                    + getSearchString(filters.get(j));
        }
        return filterSearch;
    }

    private void addPlaces(ResultSet rsQuery, String[] placeAttributes) throws SQLException{
        initializePlaces();
        int index = 0;
        while(rsQuery.next()){
            Map<String, Object> newPlace = new HashMap<>();
            for(int i = 0; i < placeAttributes.length; ++i){
                newPlace.put(setKey(placeAttributes[i]), rsQuery.getString(placeAttributes[i]));
            }
            places[index++] = newPlace;
        }
    }

    private String setKey(String key){
        String type = key.substring(0,key.indexOf('.'));
        if(type.equals("world")){
            return key.substring(key.indexOf('.') + 1);
        }
        return type;
    }

    private void initializePlaces(){
        if(limit != null){
            if(found < limit){
                this.places = new Map[found];
                return;
            }
            else if(limit != 0){
                this.places = new Map[limit];
                return;
            }
        }
        this.places = new Map[found];
    }

    @Override
    public String toString(){
        String ret = "match: " + match + "\n";
        ret += "limit: " + limit.toString() + "\n";
        ret += "found: " + found.toString() + "\n";
        ret += "places:\n";
        for(int i = 0; i < places.length; ++i){
            ret += "\t" + places[i].toString() + "\n";
        }
        return ret;
    }
}
