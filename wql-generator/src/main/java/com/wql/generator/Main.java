package com.wql.generator;

import com.wql.generator.invoker.SingleInvoker;
import com.wql.generator.invoker.base.Invoker;

public class Main {

    public static void main(String[] args) {
        single();
    }
    
    public static void single() {
        Invoker invoker = new SingleInvoker.Builder()
        		.setDbName("wql_cloud")
                .setTableName("t_order")
                .setClassName("Order")
                .build();
        invoker.execute();
    }
    
//    public static void many2many() {
//        Invoker invoker = new Many2ManyInvoker.Builder()
//                .setTableName("user")
//                .setClassName("User")
//                .setParentTableName("role")
//                .setParentClassName("Role")
//                .setRelationTableName("user_role")
//                .setForeignKey("userId")
//                .setParentForeignKey("roleId")
//                .build();
//        invoker.execute();
//    }

//    public static void one2many() {
//        Invoker invoker = new One2ManyInvoker.Builder()
//                .setTableName("user")
//                .setClassName("User")
//                .setParentTableName("office")
//                .setParentClassName("Office")
//                .setForeignKey("officeId")
//                .build();
//        invoker.execute();
//    }

}
