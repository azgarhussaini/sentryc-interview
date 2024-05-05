import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './seller-info.reducer';

export const SellerInfoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sellerInfoEntity = useAppSelector(state => state.sellerInfo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sellerInfoDetailsHeading">
          <Translate contentKey="sentryInterviewApp.sellerInfo.detail.title">SellerInfo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="sentryInterviewApp.sellerInfo.id">Id</Translate>
            </span>
          </dt>
          <dd>{sellerInfoEntity.id}</dd>
          <dt>
            <span id="marketplaceName">
              <Translate contentKey="sentryInterviewApp.sellerInfo.marketplaceName">Marketplace Name</Translate>
            </span>
          </dt>
          <dd>{sellerInfoEntity.marketplaceName}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="sentryInterviewApp.sellerInfo.url">Url</Translate>
            </span>
          </dt>
          <dd>{sellerInfoEntity.url}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="sentryInterviewApp.sellerInfo.country">Country</Translate>
            </span>
          </dt>
          <dd>{sellerInfoEntity.country}</dd>
          <dt>
            <span id="externalId">
              <Translate contentKey="sentryInterviewApp.sellerInfo.externalId">External Id</Translate>
            </span>
          </dt>
          <dd>{sellerInfoEntity.externalId}</dd>
          <dt>
            <Translate contentKey="sentryInterviewApp.sellerInfo.marketplaceId">Marketplace Id</Translate>
          </dt>
          <dd>{sellerInfoEntity.marketplaceId ? sellerInfoEntity.marketplaceId.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/seller-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seller-info/${sellerInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SellerInfoDetail;
