package com.minka;

import com.google.gson.GsonBuilder;
import com.minka.wallet.HashDto;
import com.minka.wallet.IOU;
import com.minka.wallet.IouParamsDto;
import com.minka.wallet.MetaDto;

import java.util.ArrayList;
import java.util.List;

public class IouUtil {

    public IOU write(IouParamsDto iouParamsDto)
    {
        HashDto hashDto = new HashDto();
        String data = (new GsonBuilder()).create().toJson(iouParamsDto);

        hashDto.setData(data);
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

        hashDto.setValue(HashingUtil.createHash(hashDto.getData(), types, encodings));

        MetaDto metaDto = new MetaDto();
        return new IOU(iouParamsDto, hashDto, metaDto);
    }
}
