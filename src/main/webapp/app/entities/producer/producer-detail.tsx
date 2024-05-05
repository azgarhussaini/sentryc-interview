import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './producer.reducer';

export const ProducerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const producerEntity = useAppSelector(state => state.producer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="producerDetailsHeading">
          <Translate contentKey="sentryInterviewApp.producer.detail.title">Producer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="sentryInterviewApp.producer.id">Id</Translate>
            </span>
          </dt>
          <dd>{producerEntity.id}</dd>
          <dt>
            <span id="producerName">
              <Translate contentKey="sentryInterviewApp.producer.producerName">Producer Name</Translate>
            </span>
          </dt>
          <dd>{producerEntity.producerName}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="sentryInterviewApp.producer.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{producerEntity.createdAt ? <TextFormat value={producerEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="producerLogo">
              <Translate contentKey="sentryInterviewApp.producer.producerLogo">Producer Logo</Translate>
            </span>
          </dt>
          <dd>
            {producerEntity.producerLogo ? (
              <div>
                {producerEntity.producerLogoContentType ? (
                  <a onClick={openFile(producerEntity.producerLogoContentType, producerEntity.producerLogo)}>
                    <img
                      src={`data:${producerEntity.producerLogoContentType};base64,${producerEntity.producerLogo}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {producerEntity.producerLogoContentType}, {byteSize(producerEntity.producerLogo)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/producer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/producer/${producerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProducerDetail;
