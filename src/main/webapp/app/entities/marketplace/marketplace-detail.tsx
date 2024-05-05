import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './marketplace.reducer';

export const MarketplaceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const marketplaceEntity = useAppSelector(state => state.marketplace.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="marketplaceDetailsHeading">
          <Translate contentKey="sentryInterviewApp.marketplace.detail.title">Marketplace</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="sentryInterviewApp.marketplace.id">Id</Translate>
            </span>
          </dt>
          <dd>{marketplaceEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="sentryInterviewApp.marketplace.description">Description</Translate>
            </span>
          </dt>
          <dd>{marketplaceEntity.description}</dd>
          <dt>
            <span id="marketplaceLogo">
              <Translate contentKey="sentryInterviewApp.marketplace.marketplaceLogo">Marketplace Logo</Translate>
            </span>
          </dt>
          <dd>
            {marketplaceEntity.marketplaceLogo ? (
              <div>
                {marketplaceEntity.marketplaceLogoContentType ? (
                  <a onClick={openFile(marketplaceEntity.marketplaceLogoContentType, marketplaceEntity.marketplaceLogo)}>
                    <img
                      src={`data:${marketplaceEntity.marketplaceLogoContentType};base64,${marketplaceEntity.marketplaceLogo}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {marketplaceEntity.marketplaceLogoContentType}, {byteSize(marketplaceEntity.marketplaceLogo)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/marketplace" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/marketplace/${marketplaceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MarketplaceDetail;
