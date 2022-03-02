package com.frank.types;

public class AddamsSearchCriteria {
        private String  searchValue;
        private boolean isCaseSensitiveSearch;

        public AddamsSearchCriteria(String searchValue, boolean isCaseSensitiveSearch) {
                this.searchValue = searchValue;
                this.isCaseSensitiveSearch = isCaseSensitiveSearch;
        }

        public String getSearchValue() {
                return searchValue;
        }

        public boolean isCaseSensitiveSearch() {
                return isCaseSensitiveSearch;
        }
}
