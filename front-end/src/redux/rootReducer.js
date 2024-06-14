import places from "./reducer/places/places";
import sidebar from "./reducer/sidebar/sidebar";
import chemicals from "./reducer/chemicals/chemicals";
import plants from "./reducer/plants/plants";
import waterings from "./reducer/waterings/waterings";
import gardens from "./reducer/garden/gardens";

import {combineReducers} from "redux";
import plantDetail from "./reducer/plant_detail/plant_detail";

const rootReducer = combineReducers({sidebar, places, chemicals, plants, plantDetail, waterings, gardens});

export default rootReducer;
