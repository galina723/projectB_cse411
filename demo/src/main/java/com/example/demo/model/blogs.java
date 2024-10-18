package com.example.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "blogs")
public class blogs {

    @Id
    @Column(name = "id", length = 10, nullable = false)
    private String BlogId;

    @Column(name = "title", length = 50, nullable = false)
    private String BlogTitle;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String BlogDescription;

    @Column(name = "status", length = 50, nullable = false)
    private String BlogStatus;

    @CreationTimestamp
    @Column(name = "createtime", updatable = false)
    private LocalDateTime BlogCreateDate;

    @Column(name = "postby", length = 50, nullable = false)
    private String BlogPostBy;

    @Column(name = "tag", length = 50, nullable = false)
    private String Blogtag;

    @Column(name = "image", length = 2083, nullable = false)
    private String BlogImage;

    public String getBlogId() {
        return BlogId;
    }

    public void setBlogId(String blogId) {
        BlogId = blogId;
    }

    public String getBlogTitle() {
        return BlogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        BlogTitle = blogTitle;
    }

    public String getBlogDescription() {
        return BlogDescription;
    }

    public void setBlogDescription(String blogDescription) {
        BlogDescription = blogDescription;
    }

    public String getBlogStatus() {
        return BlogStatus;
    }

    public void setBlogStatus(String blogStatus) {
        BlogStatus = blogStatus;
    }

    public LocalDateTime getBlogCreateDate() {
        return BlogCreateDate;
    }

    public void setBlogCreateDate(LocalDateTime blogCreateDate) {
        BlogCreateDate = blogCreateDate;
    }

    public String getBlogPostBy() {
        return BlogPostBy;
    }

    public void setBlogPostBy(String blogPostBy) {
        BlogPostBy = blogPostBy;
    }

    public String getBlogtag() {
        return Blogtag;
    }

    public void setBlogtag(String blogtag) {
        Blogtag = blogtag;
    }

    public String getBlogImage() {
        return BlogImage;
    }

    public void setBlogImage(String blogImage) {
        BlogImage = blogImage;
    }

}
