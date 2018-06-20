package com.csu.campusmap.Util;

import android.app.Activity;

import com.csu.campusmap.Main.MenuActivity;
import com.csu.campusmap.Model.Data;
import com.csu.campusmap.R;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

/**
 * Created by Ung Man Pak on 3/5/2016.
 */
public class SearchUtil implements SearchBox.SearchListener,SearchBox.MenuListener {

    private SearchBox searchBox;
    private MenuActivity context;

    private SearchUtil(SearchBox searchBox){

        searchBox.enableVoiceRecognition((Activity)searchBox.getContext());
        searchBox.setLogoText("Search");
        searchBox.setMenuListener(this);
        searchBox.setSearchListener(this);
        this.searchBox = searchBox;
        this.context = ((MenuActivity)searchBox.getContext());
        this.context.getWindow().setSoftInputMode(48);

    }

    public void addSearchables(ArrayList<Data> datas){
        for (int i = 0 ; i< datas.size(); i++){
            SearchResult option = new SearchResult("",context.getResources().getDrawable(R.drawable.ic_history));
        }
    }

    @Override
    public void onMenuClick() {

    }

    @Override
    public void onSearchOpened() {

    }

    @Override
    public void onSearchCleared() {

    }

    @Override
    public void onSearchClosed() {

    }

    @Override
    public void onSearchTermChanged(String s) {

    }

    @Override
    public void onSearch(String s) {

    }

    @Override
    public void onResultClick(SearchResult searchResult) {

    }
}
