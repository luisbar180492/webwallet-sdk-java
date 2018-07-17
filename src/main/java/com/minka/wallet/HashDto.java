package com.minka.wallet;

import java.util.List;

public class HashDto {

    private List<String> types;//hash algorithms
    private List<String> steps;
    private String value;


    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HashJson getDtoForJson() {

        HashJson result = new HashJson();

        for (String curr:this.getTypes()) {
            if (result.getTypes() == null){
                result.setTypes(curr);
            } else{
                result.setTypes(result.getTypes() + ":" + curr);
            }
        }
        for (String curr: this.getSteps()) {
            if (result.getSteps() == null){
                result.setSteps(curr);
            } else{
                result.setSteps(result.getSteps() + ":" + curr);
            }
        }

        result.setValue(this.getValue());

        return result;
    }

    public class HashJson {
        private String types;
        private String steps;
        private String value;

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public String getSteps() {
            return steps;
        }

        public void setSteps(String steps) {
            this.steps = steps;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    }
