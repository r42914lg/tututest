package com.r42914lg.tutu.service;

import com.r42914lg.tutu.domain.Category;
import com.r42914lg.tutu.domain.CategoryDetailed;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API_JService {

    @GET("categories")
    Call<List<Category>> getCategories(@Query("count") int amountOfCluesToReturn, @Query("offset") int offset);

    @GET("category")
    Call<CategoryDetailed> getDetailedCategory(@Query("id") int categoryId);
}
