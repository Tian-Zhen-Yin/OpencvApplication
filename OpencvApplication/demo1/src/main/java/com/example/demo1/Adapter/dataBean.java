package com.example.demo1.Adapter;

public class dataBean {
    public long id;
    public String command;
    public String name;

    public dataBean(String command,long id)
    {
        this.command=command;
        this.id=id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
