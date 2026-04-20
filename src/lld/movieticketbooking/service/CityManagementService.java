package lld.movieticketbooking.service;

import lld.movieticketbooking.model.City;
import sun.java2d.x11.X11SurfaceData;

import java.util.*;

public class CityManagementService {
    Map<Integer, City> mapofCities=new HashMap<>();

    public City   addCity(City city){
        mapofCities.put(city.getId(),city);
        return city;
    }

    public City getCity(Integer cityId){
        if (mapofCities.containsKey(cityId)){
            return mapofCities.get(cityId);
        }else {
            return null;
        }
    }
    public List<City> getListOfCities(){
        List<City> cities=new ArrayList<>();
        for(Map.Entry<Integer,City> m:mapofCities.entrySet()){
            cities.add(m.getValue());
        }
        return cities;
    }

}
