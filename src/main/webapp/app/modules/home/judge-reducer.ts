// import axios from 'axios';
// import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';
//
// import {IUser} from "app/shared/model/user.model";
// import { getUsers} from "app/modules/administration/user-management/user-management.reducer";
//
// import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
//
// export const ACTION_TYPES = {
//     CREATE_JUDGE: 'userManagement/CREATE_USER'
// };
// const apiUrl = 'api/judge';
//
// /*// Reducer
// export default (state: UserManagementState = initialState, action): UserManagementState => {
//     switch (action.type) {
//         case REQUEST(ACTION_TYPES.FETCH_ROLES):
//             return {
//                 ...state
//             };
//         case REQUEST(ACTION_TYPES.FETCH_USERS):
//         case REQUEST(ACTION_TYPES.FETCH_USER):
//             return {
//                 ...state,
//                 errorMessage: null,
//                 updateSuccess: false,
//                 loading: true
//             };
//         case REQUEST(ACTION_TYPES.CREATE_USER):
//         case REQUEST(ACTION_TYPES.UPDATE_USER):
//         case REQUEST(ACTION_TYPES.DELETE_USER):
//             return {
//                 ...state,
//                 errorMessage: null,
//                 updateSuccess: false,
//                 updating: true
//             };
//         case FAILURE(ACTION_TYPES.FETCH_USERS):
//         case FAILURE(ACTION_TYPES.FETCH_USER):
//         case FAILURE(ACTION_TYPES.FETCH_ROLES):
//         case FAILURE(ACTION_TYPES.CREATE_USER):
//         case FAILURE(ACTION_TYPES.UPDATE_USER):
//         case FAILURE(ACTION_TYPES.DELETE_USER):
//             return {
//                 ...state,
//                 loading: false,
//                 updating: false,
//                 updateSuccess: false,
//                 errorMessage: action.payload
//             };
//         case SUCCESS(ACTION_TYPES.FETCH_ROLES):
//             return {
//                 ...state,
//                 authorities: action.payload.data
//             };
//         case SUCCESS(ACTION_TYPES.FETCH_USERS):
//             return {
//                 ...state,
//                 loading: false,
//                 users: action.payload.data,
//                 totalItems: action.payload.headers['x-total-count']
//             };
//         case SUCCESS(ACTION_TYPES.FETCH_USER):
//             return {
//                 ...state,
//                 loading: false,
//                 user: action.payload.data
//             };
//         case SUCCESS(ACTION_TYPES.CREATE_USER):
//         case SUCCESS(ACTION_TYPES.UPDATE_USER):
//             return {
//                 ...state,
//                 updating: false,
//                 updateSuccess: true,
//                 user: action.payload.data
//             };
//         case SUCCESS(ACTION_TYPES.DELETE_USER):
//             return {
//                 ...state,
//                 updating: false,
//                 updateSuccess: true,
//                 user: defaultValue
//             };
//         case ACTION_TYPES.RESET:
//             return {
//                 ...initialState
//             };
//         default:
//             return state;
//     }
// };*/
//
//
// /*export const createJudge: ICrudPutAction<String> = user => async dispatch => {
//     const result = await dispatch({
//         type: ACTION_TYPES.CREATE_USER,
//         payload: axios.post(apiUrl, user)
//     });
//     dispatch(getUsers());
//     return result;
// };*/
