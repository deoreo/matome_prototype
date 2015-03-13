package com.jds.webapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JDS on 13/03/2015.
 */
public class Filter {
    private ItemFilter mFilter;
    List<DataArticle> mSourceData;

    public Filter(List<DataArticle> d){
        mSourceData =d;
    }

    public List<DataArticle> getFilter(String keyword) {
        mFilter = new ItemFilter(keyword);
        List<DataArticle> result = mFilter.performFiltering();
        return result;
    }

    private class ItemFilter {
        String keyword;
        public ItemFilter(String keyword){
            this.keyword = keyword;
        }

        public List<DataArticle> performFiltering() {
            String filterString = keyword.toLowerCase();
            List<DataArticle> listData = mSourceData;
            int count = listData.size();
            final ArrayList<DataArticle> resultList = new ArrayList<DataArticle>(count);

            DataArticle dataArticle ;
            for (int i = 0; i < count; i++) {
                dataArticle = listData.get(i);
                if (dataArticle.getContent().toLowerCase().contains(filterString)) {
                    resultList.add(dataArticle);
                }
            }

            return resultList;
        }
    }
}
