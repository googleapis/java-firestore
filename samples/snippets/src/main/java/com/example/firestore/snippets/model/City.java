/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.firestore.snippets.model;

import java.util.List;
import java.util.Objects;

// [START fs_class_definition]
// [START firestore_data_custom_type_definition]
/** Represents a city : name, weather, population, country, capital, geo coordinates. */
public class City {

  private String name;
  private String state;
  private String country;
  private Boolean capital;
  private Long population;
  private List<String> regions;

  public City() {
    // Must have a public no-argument constructor
  }

  public City(
      String name,
      String state,
      String country,
      Boolean capital,
      Long population,
      List<String> regions) {
    // Initialize all fields of a city
    // [START_EXCLUDE]
    this.name = name;
    this.state = state;
    this.country = country;
    this.capital = capital;
    this.population = population;
    this.regions = regions;
    // [END_EXCLUDE]
  }

  // public getters and setters for all fields
  // [START_EXCLUDE]
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Boolean getCapital() {
    return capital;
  }

  public void setCapital(Boolean capital) {
    this.capital = capital;
  }

  public Long getPopulation() {
    return population;
  }

  public void setPopulation(Long population) {
    this.population = population;
  }

  public List<String> getRegions() {
    return regions;
  }

  public void setRegions(List<String> regions) {
    this.regions = regions;
  }
  // [END_EXCLUDE]

  // [START_EXCLUDE]
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (name != null) {
      sb.append(" name : ").append(name).append(",");
    }
    if (state != null) {
      sb.append(" state : ").append(state).append(",");
    }
    if (country != null) {
      sb.append(" country : ").append(country).append(",");
    }
    if (population != null) {
      sb.append(" population : ").append(population).append(",");
    }
    if (capital != null) {
      sb.append(" capital : ").append(capital).append(",");
    }
    if (regions != null) {
      sb.append(" regions : ").append(regions).append(",");
    }
    // remove trailing comma
    if (sb.length() > 0) {
      sb.setLength(sb.length() - 1);
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null) return false;
    if (obj.getClass() != this.getClass()) return false;
    
    City city = (City) obj;
    return Objects.equals(name, city.name)
        && Objects.equals(state, city.state)
        && Objects.equals(country, city.country)
        && Objects.equals(population, city.population)
        && Objects.equals(capital, city.capital)
        && Objects.equals(regions, city.regions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, state, country, capital, population, regions);
  }
  // [END_EXCLUDE]
}
// [END firestore_data_custom_type_definition]
// [END fs_class_definition]
