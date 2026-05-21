package com.project.artconnect.model;

public class Discipline {
    private int disciplineId;
    private String name;

    public Discipline() {
    }

    public Discipline(String name) {
        this.name = name;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
