package com.solidarix.backend.model;

/**
 * Représente les différents états qu'une offre d'aide (HelpOffer) peut traverser
 * dans le cadre d'une entraide sur la plateforme Solidarix.
 */
public enum HelpOfferStatus {

    /**
     * Étape 1 — L'utilisateur s'est proposé pour aider,
     * et la discussion avec le demandeur est en cours.
     */
    PROPOSED


    /**
     * Le demandeur a validé l'aide proposée,
     * en attente de la confirmation définitive du proposeur.
     */
    , VALIDATED_BY_REQUESTER


    /**
     * Le proposeur a confirmé sa disponibilité après validation par le demandeur.
     * L'entraide est considérée comme confirmée et engagée des deux côtés.
     */
    , CONFIRMED_BY_HELPER


    /**
     * Le demandeur a annulé l’offre d’aide (avant validation ou après validation et avant confirmation ou après confirmation).
     * Une justification peut être exigée.
     * L'impact sur le score social varie selon le moment d'annulation (statut de la proposition) et la justifiction
     */
    , CANCELED_BY_REQUESTER


    /**
     * Le proposeur annule sa proposition (avant la validation ou après la validation et avant confirmation ou après confirmation).
     * Une justifiction peut-être exigée selon le statut de la proposition
     * L'impact sur le score social varie selon le moment d'annulation (statut de la proposition) et la justifiction
     */
    , CANCELED_BY_HELPER


    /**
     * L’un des délais a expiré :
     * - soit le demandeur n’a pas validé l’offre dans le temps imparti,
     * - soit le proposeur n’a pas confirmé après validation.
     */
    , EXPIRED


    /**
     * L'entraide s'est déroulée avec succès, clôturée et validée comme accomplie.
     */
    , DONE

    /**
     * L'entraide s’est soldée par un échec (désistement injustifié, abandon,
     * ou incident signalé par l'une des deux parties).
     */
    , FAILED

}
