/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.condoorders;

public class UserDetailsFireStoreItems {

    private String fullName;
    private double userBonus;
    private String photoUrl;
    private String address;

   // public UserDetailsFireStoreItems(String name, double finalSolutionForBonusValue, Object photoUrl, String address) {
   // }



    public UserDetailsFireStoreItems(String fullName, double userBonus) {
        this.fullName = fullName;
        this.userBonus = userBonus;
        this.photoUrl = photoUrl;
       this.address = address;
    }



    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getUserBonus() {
        return userBonus;
    }

    public void setUserBonus(double userBonus) {
        this.userBonus = userBonus;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



}
