package com.suse.hwj.springboot.entity;

public class Equipment {
    private Integer equipmentId;
    private String equipmentName;
    private String equipmentImage;
    private String instructions;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentImage() {
        return equipmentImage;
    }

    public void setEquipmentImage(String equipmentImage) {
        this.equipmentImage = equipmentImage;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
