package com.example.matthewfarley.rsstest.Models;

import java.io.Serializable;
import java.net.MalformedURLException;

public class Article implements Serializable {
	
	public static final String KEY_ARTICLE = "ARTICLE";


	private static final long serialVersionUID = 1L;

	private String guid;
	private String title;
	private String description;
	private String pubDate;
	private String author;
	private String url;
	private String encodedContent;
	private boolean read;
	private boolean offline;
	private long dbId;
	private String thumbnailURL;

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailUrl) {
		if(!thumbnailUrl.contains("http:")){
			thumbnailUrl = "http:" + thumbnailUrl;
		}
		this.thumbnailURL = thumbnailUrl;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url)throws MalformedURLException{
		this.url = url;
	}

	public void setDescription(String description) {
		this.description = extractCData(description);
	}

	public String getDescription() {
		return description;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setEncodedContent(String encodedContent) {
		this.encodedContent = extractCData(encodedContent);
	}

	public String getEncodedContent() {
		return encodedContent;
	}
	
	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isOffline() {
		return offline;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}


	private String extractCData(String data){
		data = data.replaceAll("<!\\[CDATA\\[", "");
		data = data.replaceAll("\\]\\]>", "");
		return data;
	}

}