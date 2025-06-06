/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ApiResponseListUserResponse } from '../../models/api-response-list-user-response';

export interface GetAllUsersExceptSelf$Params {
}

export function getAllUsersExceptSelf(http: HttpClient, rootUrl: string, params?: GetAllUsersExceptSelf$Params, context?: HttpContext): Observable<StrictHttpResponse<ApiResponseListUserResponse>> {
  const rb = new RequestBuilder(rootUrl, getAllUsersExceptSelf.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<ApiResponseListUserResponse>;
    })
  );
}

getAllUsersExceptSelf.PATH = '/api/v1/users/except-self';
