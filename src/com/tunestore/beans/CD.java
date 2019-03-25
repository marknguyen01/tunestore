package com.tunestore.beans;

public class CD {
  private Long id;
  private String artist;
  private String album;
  private String image;
  private Double price;
  private String bits;
  private boolean owned;
  
  public boolean isOwned() {
    return owned;
  }
  public void setOwned(boolean owned) {
    this.owned = owned;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getArtist() {
    return artist;
  }
  public void setArtist(String artist) {
    this.artist = artist;
  }
  public String getAlbum() {
    return album;
  }
  public void setAlbum(String album) {
    this.album = album;
  }
  public Double getPrice() {
    return price;
  }
  public void setPrice(Double price) {
    this.price = price;
  }
  public String getImage() {
    return image;
  }
  public void setImage(String image) {
    this.image = image;
  }
  public String getBits() {
    return bits;
  }
  public void setBits(String bits) {
    this.bits = bits;
  }
}
