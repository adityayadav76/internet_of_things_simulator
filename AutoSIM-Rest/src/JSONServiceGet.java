/*
 *  AutoSIM - Internet of Things Simulator
 *  Copyright (C) 2014, Aditya Yadav <aditya@automatski.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Random;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
public class JSONServiceGet extends ServerResource{

	@Get("json")
	public Product getProductInJSON() {

		Product product = new Product();
		Random rnd = new Random();
		product.setName("iPad "+(rnd.nextInt(6)+1));
		product.setQty(rnd.nextInt(999));
		
		return product; 

	}
	
}