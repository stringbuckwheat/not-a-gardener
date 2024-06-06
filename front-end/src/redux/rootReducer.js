import places from "./reducer/places";
import sidebar from "./reducer/sidebar";
import chemicals from "./reducer/chemicals";
import plants from "./reducer/plants";
import waterings from "./reducer/waterings";
import gardens from "./reducer/gardens";

import {combineReducers} from "redux";
import plantDetail from "./reducer/plant_detail";

const rootReducer = combineReducers({sidebar, places, chemicals, plants, plantDetail, waterings, gardens});

export default rootReducer;
