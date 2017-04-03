package com.kmema.android.movienow;



public class MovieDBReview {

    String reviewAuthor;
        String reviewContent;
        String reviewURL;

        MovieDBReview(String reviewId,String reviewAuthor,String reviewContent,String reviewURL)
        {
            String reviewId1 = reviewId;
            this.reviewAuthor = reviewAuthor;
            this.reviewContent = reviewContent;
            this.reviewURL = reviewURL;
        }
    }


