import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProducer } from 'app/shared/model/producer.model';
import { getEntities as getProducers } from 'app/entities/producer/producer.reducer';
import { ISellerInfo } from 'app/shared/model/seller-info.model';
import { getEntities as getSellerInfos } from 'app/entities/seller-info/seller-info.reducer';
import { ISeller } from 'app/shared/model/seller.model';
import { SellerState } from 'app/shared/model/enumerations/seller-state.model';
import { getEntity, updateEntity, createEntity, reset } from './seller.reducer';

export const SellerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const producers = useAppSelector(state => state.producer.entities);
  const sellerInfos = useAppSelector(state => state.sellerInfo.entities);
  const sellerEntity = useAppSelector(state => state.seller.entity);
  const loading = useAppSelector(state => state.seller.loading);
  const updating = useAppSelector(state => state.seller.updating);
  const updateSuccess = useAppSelector(state => state.seller.updateSuccess);
  const sellerStateValues = Object.keys(SellerState);

  const handleClose = () => {
    navigate('/seller' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProducers({}));
    dispatch(getSellerInfos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    const entity = {
      ...sellerEntity,
      ...values,
      producerId: producers.find(it => it.id.toString() === values.producerId?.toString()),
      sellerInfoId: sellerInfos.find(it => it.id.toString() === values.sellerInfoId?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          state: 'REGULAR',
          ...sellerEntity,
          producerId: sellerEntity?.producerId?.id,
          sellerInfoId: sellerEntity?.sellerInfoId?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sentryInterviewApp.seller.home.createOrEditLabel" data-cy="SellerCreateUpdateHeading">
            <Translate contentKey="sentryInterviewApp.seller.home.createOrEditLabel">Create or edit a Seller</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="seller-id"
                  label={translate('sentryInterviewApp.seller.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sentryInterviewApp.seller.sellerName')}
                id="seller-sellerName"
                name="sellerName"
                data-cy="sellerName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sentryInterviewApp.seller.state')}
                id="seller-state"
                name="state"
                data-cy="state"
                type="select"
              >
                {sellerStateValues.map(sellerState => (
                  <option value={sellerState} key={sellerState}>
                    {translate('sentryInterviewApp.SellerState.' + sellerState)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="seller-producerId"
                name="producerId"
                data-cy="producerId"
                label={translate('sentryInterviewApp.seller.producerId')}
                type="select"
                required
              >
                <option value="" key="0" />
                {producers
                  ? producers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="seller-sellerInfoId"
                name="sellerInfoId"
                data-cy="sellerInfoId"
                label={translate('sentryInterviewApp.seller.sellerInfoId')}
                type="select"
                required
              >
                <option value="" key="0" />
                {sellerInfos
                  ? sellerInfos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/seller" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SellerUpdate;
