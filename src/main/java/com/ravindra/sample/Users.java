package com.ravindra.sample;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="dbdemo")	
public class Users {	
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

@NotNull
private String input;


@Column
private Date created_at;

public Users()
{}

public Users(String input) 
{
	this.input = input;
	
}

public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}

public String getInput()
{
	return input;
}

public void setInput(String input)
{

this.input=input;	
}


@Override
public String toString() {
	return "Users [id=" + id + ", input=" + input +"]";
}


}
