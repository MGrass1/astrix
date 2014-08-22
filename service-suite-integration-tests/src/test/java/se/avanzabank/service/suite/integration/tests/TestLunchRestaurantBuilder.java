/*
 * Copyright 2014-2015 Avanza Bank AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.avanzabank.service.suite.integration.tests;

import se.avanzabank.service.suite.integration.tests.domain.api.LunchRestaurant;

public class TestLunchRestaurantBuilder {
	
	private String foodType;
	private String name;
	
	public static TestLunchRestaurantBuilder lunchRestaurant() {
		TestLunchRestaurantBuilder result = new TestLunchRestaurantBuilder();
		result.foodType = ("vegetarian");
		result.name = "Martins Green Room";
		return result;
	}
	
	public TestLunchRestaurantBuilder withFoodType(String foodType) {
		this.foodType = foodType;
		return this;
	}
	
	public TestLunchRestaurantBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public LunchRestaurant build() {
		LunchRestaurant result = new LunchRestaurant();
		result.setFoodType(foodType);
		result.setName(name);
		return result;
	}

}
