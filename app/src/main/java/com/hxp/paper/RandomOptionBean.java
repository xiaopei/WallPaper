package com.hxp.paper;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/22.
 */
public class RandomOptionBean implements Serializable {
        public String typeId;
        public String typeName; // 类型
        public String selected;

        public RandomOptionBean( String typeId,String type){
            this.typeId = typeId;
            this.typeName = type;
            selected = "1";
        }


        @Override
        public String toString() {
            return "Msg{" +
                    "typeId='" + typeId + '\'' +
                    ", typeName='" + typeName + '\'' +
                    ", selected=" + selected +
                    '}';
        }
}
