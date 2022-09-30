/**
 * Copyright (c) 2016-2020, Michael Yang Fuhai (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.commons.wordsfilter.algorithm;

import java.util.HashSet;
import java.util.Set;


public class DFAConfig {

    /**
     * Whether to support Pinyin, configuration Chinese characters can only identify Chinese characters, and cannot recognize Pinyin
     */
    private boolean supportPinyin = false;
    /**
     * Ignore a lowercase, the default is sensitive
     */
    private boolean ignoreCase = false;
    /**
     * Support simplified, traditional, default does not support, and simplified configuration can only identify simplified parts
     */
    private boolean supportSimpleTraditional = false;
    /**
     * Support the half-angle full-angle, the default does not support it, the configuration can only be recognized by the half -angle
     */
    private boolean supportDbc = false;
    /**
     * Support pause, do not support the default, use other characters between words to separate and do not recognize
     */
    private boolean supportStopWord = false;

    private String stopWord = "";

    private Set<Character> stopWordCharSet = new HashSet<>();

    public boolean isSupportPinyin() {
        return supportPinyin;
    }

    public void setSupportPinyin(boolean supportPinyin) {
        this.supportPinyin = supportPinyin;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean isSupportSimpleTraditional() {
        return supportSimpleTraditional;
    }

    public void setSupportSimpleTraditional(boolean supportSimpleTraditional) {
        this.supportSimpleTraditional = supportSimpleTraditional;
    }

    public boolean isSupportDbc() {
        return supportDbc;
    }

    public void setSupportDbc(boolean supportDbc) {
        this.supportDbc = supportDbc;
    }

    public boolean isSupportStopWord() {
        return supportStopWord;
    }

    public void setSupportStopWord(boolean supportStopWord) {
        this.supportStopWord = supportStopWord;
    }

    public String getStopWord() {
        return stopWord;
    }

    public void setStopWord(String stopWord) {
        this.stopWord = stopWord;
        this.stopWordCharSet.clear();
        if (this.stopWord != null) {
            for (char c : this.stopWord.toCharArray()) {
                stopWordCharSet.add(c);
            }
        }
    }

    public boolean containsStopChar(char ch) {
        return this.stopWordCharSet.contains(ch);
    }

    public DFAConfig() {

    }

    public static class Builder {

        private boolean supportPinyin = false;

        private boolean ignoreCase = false;

        private boolean supportSimpleTraditional = false;

        private boolean supportDbc = false;

        private boolean supportStopWord = false;

        private String stopWord = "";

        public Builder() {

        }

        /**
         * Whether to support Pinyin, configuration Chinese characters can only identify Chinese characters, and cannot recognize Pinyin
         */
        public Builder setSupportPinyin(boolean support) {
            this.supportPinyin = support;
            return this;
        }

        /**
         * Ignore a lowercase, the default is sensitive
         */
        public Builder setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
            return this;
        }

        /**
         * Support simplified, traditional, default does not support, and simplified configuration can only identify simplified parts
         */
        public Builder setSupportSimpleTraditional(boolean supportSimpleTraditional) {
            this.supportSimpleTraditional = supportSimpleTraditional;
            return this;
        }

        /**
         * Support the half-angle full-angle, the default does not support it, the configuration can only be recognized by the half-angle
         */
        public Builder setSupportDbc(boolean supportDbc) {
            this.supportDbc = supportDbc;
            return this;
        }

        /**
         * Support pause, do not support the default, use other characters between words to separate and do not recognize
         */
        public Builder setSupportStopWord(boolean supportStopWord) {
            this.supportStopWord = supportStopWord;
            return this;
        }

        /**
         * Set up pause words
         */
        public Builder setStopWord(String stopWord) {
            this.stopWord = stopWord;
            return this;
        }

        public DFAConfig build() {
            DFAConfig config = new DFAConfig();
            config.setIgnoreCase(this.ignoreCase);
            config.setSupportStopWord(this.supportStopWord);
            config.setStopWord(this.stopWord);
            config.setSupportDbc(this.supportDbc);
            config.setSupportPinyin(this.supportPinyin);
            config.setSupportSimpleTraditional(this.supportSimpleTraditional);
            return config;
        }
    }
}
