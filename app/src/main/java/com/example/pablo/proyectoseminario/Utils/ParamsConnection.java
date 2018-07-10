package com.example.pablo.proyectoseminario.Utils;

import com.example.pablo.proyectoseminario.ListDataSource.ItemList;
import com.example.pablo.proyectoseminario.ListDataSource.ItemListSchool;
import com.example.pablo.proyectoseminario.ListDataSource.ItemListVecindario;

import java.util.ArrayList;

public class ParamsConnection {
    static public ArrayList<ItemListVecindario>LISTVECINDARIO;
    static public ArrayList<ItemList> LISTDATA;
    static public ArrayList<ItemListSchool> LISTDATASCHOOL;
    static public String HOSTSCHOOL = "http://192.168.43.109:7777/api/v1.0/escuelas/";
    static public String HOST = "http://192.168.43.109:7777/api/v1.0/homes/";
    static public String HOST2 = "http://192.168.43.109:7777";
    static public String REST_USERIMG_POST = "http://192.168.43.109:7777/api/v1.0/homeimg";
    static public String REST_HOME_PATCH = "http://192.168.43.109:7777/api/v1.0/homes/";
    static public String HOST_VECINDARIO = "http://192.168.43.109:7777/api/v1.0/vecindario/";
}
