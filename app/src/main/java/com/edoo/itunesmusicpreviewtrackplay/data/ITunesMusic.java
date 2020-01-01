package com.edoo.itunesmusicpreviewtrackplay.data;

public class ITunesMusic {
//    public String wrapperType;
//    public String kind;
//    public int artistId;
//    public int collectionId;
//    public int trackId;
    public String artistName;
    public String collectionName;
    public String trackName;
//    public String collectionCensoredName;
//    public String trackCensoredName;
//    public String artistViewUrl;
//    public String collectionViewUrl;
//    public String trackViewUrl;
    public String previewUrl;
//    public String artworkUrl30;
//    public String artworkUrl60;
    public String artworkUrl100;
//    public String collectionPrice;
//    public String trackPrice;
//    public String releaseDate;
//    public String collectionExplicitness;
//    public String trackExplicitness;
//    public String discCount;
//    public String discNumber;
//    public String trackCount;
//    public String trackNumber;
//    public String trackTimeMillis;
//    public String country;
//    public String currency;
//    public String primaryGenreName;
    public boolean isStreamable;

    public ITunesMusic(String previewUrl, String artworkUrl100) {
        this.previewUrl = previewUrl;
        this.artworkUrl100 = artworkUrl100;
    }

}
