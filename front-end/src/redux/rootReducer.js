import places from "./reducer/places";
import sidebar from "./reducer/sidebar";
import chemicals from "./reducer/chemicals";
import plants from "./reducer/plants";

import {combineReducers} from "redux";

const rootReducer = combineReducers({sidebar, places, chemicals, plants});

export default rootReducer;
