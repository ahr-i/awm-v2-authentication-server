package com.example.authenticationserver.Mapper;
import org.modelmapper.ModelMapper;
public class TransferClass {


    public static <T,R> R getTransfer(T sourceClass , Class<R> destinationClass){
        ModelMapper mapper = new ModelMapper();
        R map = mapper.map(sourceClass, destinationClass);
        return map;
    }
}