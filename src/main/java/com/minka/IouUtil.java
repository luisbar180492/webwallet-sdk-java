package com.minka;

import com.google.gson.GsonBuilder;
import com.minka.wallet.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class IouUtil {


    public String convertToIsoFormat(Date date){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public IOU write(IouParamsDto iouParamsDto)
    {
        HashDto hashDto = new HashDto();

        List<String> steps = new ArrayList<>();
        steps.add("stringfy");
        steps.add("data");
        hashDto.setSteps(steps);

        List<String> types = new ArrayList<>();
        types.add("sha256");
        types.add("sha256");

        hashDto.setTypes(types);

        List<String> encodings = new ArrayList<>();
        encodings.add("utf8");

        String data = (new GsonBuilder()).create().toJson(iouParamsDto);
        hashDto.setValue(HashingUtil.createHashForIou(data, types, encodings));

        return new IOU(iouParamsDto, hashDto.getDtoForJson());
    }
}
