/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.chinese;

/**
 * Created by luog on 15/4/13.
 */
public final class Character implements Comparable<Character> {
    /**
     * 字
     */
    private char character;
    /**
     * 拼音列表
     */
    private String[] spell;
    /**
     * 是否可以单字成词
     */
    private boolean singleWord;

    /**
     * 词性
     */
    private String[] wordClass;

    public Character() {

    }

    public Character(char character) {
        this.character = character;
    }

    public Character(char character, String[] spell, boolean singleWord,
                     String[] wordClass) {
        this.character = character;
        this.spell = spell;
        this.singleWord = singleWord;
        this.wordClass = wordClass;
    }

    public String[] getWordClass() {
        return wordClass;
    }

    public void setWordClass(String[] wordClass) {
        this.wordClass = wordClass;
    }

    public boolean isSingleWord() {
        return singleWord;
    }

    public void setSingleWord(boolean singleWord) {
        this.singleWord = singleWord;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public String[] getSpell() {
        return spell;
    }

    public void setSpell(String[] spell) {
        this.spell = spell;
    }

    public int compareTo(Character o) {
        if (character == o.character) {
            return 0;
        }
        if (character > o.character) {
            return 1;
        } else {
            return -1;
        }
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + character;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Character other = (Character) obj;
        if (character != other.character) {
            return false;
        }
        return true;
    }

}