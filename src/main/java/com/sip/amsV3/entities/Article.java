package com.sip.amsV3.entities;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity

public class Article {
	
	@Override
	public String toString() {
		return "Article [id=" + id + ", label=" + label + ", price=" + price + ", picture=" + picture + ", image="
				+ Arrays.toString(image) + ", provider=" + provider + "]";
	}


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Label is mandatory")
    @Column(name = "label")
    private String label;
    
    
    @Min(value = 100)
    @Column(name = "price")
    private float price;
    
    @Column(name = "picture")
    private String picture;

    @Lob
    @Column(name="image")
    private byte[] image;

    public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Article() {}

    public Article(String label, float price, String picture, byte[] image) {
        this.price = price;
        this.label = label;
        this.picture = picture;
        this.image = image;
        }

    public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	
	/**** Many To One ****/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Provider provider;
	
	
	public Provider getProvider() {
    	return provider;
    }
    
    public void setProvider(Provider provider) {
    	this.provider=provider;
    }  
    
}
