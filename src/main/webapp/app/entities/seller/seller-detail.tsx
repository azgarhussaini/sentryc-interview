import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './seller.reducer';

export const SellerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sellerEntity = useAppSelector(state => state.seller.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sellerDetailsHeading">
          <Translate contentKey="sentryInterviewApp.seller.detail.title">Seller</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="sentryInterviewApp.seller.id">Id</Translate>
            </span>
          </dt>
          <dd>{sellerEntity.id}</dd>
          <dt>
            <span id="sellerName">
              <Translate contentKey="sentryInterviewApp.seller.sellerName">Seller Name</Translate>
            </span>
          </dt>
          <dd>{sellerEntity.sellerName}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="sentryInterviewApp.seller.state">State</Translate>
            </span>
          </dt>
          <dd>{sellerEntity.state}</dd>
          <dt>
            <Translate contentKey="sentryInterviewApp.seller.producerId">Producer Id</Translate>
          </dt>
          <dd>{sellerEntity.producerId ? sellerEntity.producerId.id : ''}</dd>
          <dt>
            <Translate contentKey="sentryInterviewApp.seller.sellerInfoId">Seller Info Id</Translate>
          </dt>
          <dd>{sellerEntity.sellerInfoId ? sellerEntity.sellerInfoId.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/seller" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seller/${sellerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SellerDetail;
