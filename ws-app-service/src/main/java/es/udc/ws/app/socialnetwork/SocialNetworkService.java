package es.udc.ws.app.socialnetwork;

import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.socialnetwork.exceptions.AuthenticationException;
import es.udc.ws.app.socialnetwork.exceptions.DuplicatedPostException;
import es.udc.ws.app.socialnetwork.exceptions.PostNotFountException;
import es.udc.ws.util.exceptions.InputValidationException;

public interface SocialNetworkService
{
    SocialNetworkShowPost publishShow(Show show) throws InputValidationException, AuthenticationException, DuplicatedPostException;

    SocialNetworkShowPost updateShow(Show show) throws PostNotFountException, AuthenticationException;

    SocialNetworkShowPost getShow(Show show) throws PostNotFountException, AuthenticationException;

    void commentShow(Show show, String comment) throws InputValidationException, AuthenticationException, PostNotFountException;
}
