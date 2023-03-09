import React, { useEffect, useState } from 'react'
import {CTable,
    CTableHead,
    CTableHeaderCell,
    CTableRow,
    CTableBody,
    CTableDataCell,
    CNav,
    CNavItem,
    CNavLink,
  } from '@coreui/react'

const PlantLog = (props) => {
    const waterList = props;

    if(props.waterList.length === 0){
      return (
        <div className="mt-3">아직 물주기 기록이 없어요.</div>
      )
    }

    return(
      <>
      <h2>식물 로그</h2>
      <CNav variant="tabs">
          <CNavItem>
            <CNavLink>
              물주기 기록
            </CNavLink>
          </CNavItem>
          <CNavItem>
            <CNavLink href="#">Link</CNavLink>
          </CNavItem>
          <CNavItem>
            <CNavLink href="#">Link</CNavLink>
          </CNavItem>
          <CNavItem>
            <CNavLink href="#" disabled>
              Disabled
            </CNavLink>
          </CNavItem>
        </CNav>
            
            <CTable>
              <CTableHead>
                <CTableRow>
                  <CTableHeaderCell scope="col"></CTableHeaderCell>
                  <CTableHeaderCell scope="col">물 준 날짜</CTableHeaderCell>
                  <CTableHeaderCell scope="col">비료</CTableHeaderCell>
                </CTableRow>
              </CTableHead>
              <CTableBody>
                    {props.waterList.map((water, idx) => {
                      const fertilized = water.fertilized ? water.fertilized : "N";
                      const color = fertilized == "N" ? "" : "warning";

                      return(
                        <CTableRow color={color}>
                          <CTableDataCell>{idx + 1}</CTableDataCell>
                          <CTableHeaderCell scope="row">{water.wateringDate}</CTableHeaderCell>
                          <CTableDataCell>{fertilized}</CTableDataCell>
                        </CTableRow>
                      )
                    })}
              </CTableBody>
            </CTable>
            </>
    )
}

export default PlantLog