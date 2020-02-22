package student.server;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import student.adventure.Adventure;


@Path("/")
public class AdventureResource {
    /**
     * Helper method to build an `instanceNotFound` error.
     * @param id the id given.
     */
    private Response instanceNotFound(int id) {
        return Response
                .status(Status.BAD_REQUEST)
                .entity(new Error("No game found with id '" + id + "'."))
                .build();
    }

    @GET
    @Path("ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        // TODO: This method should return `pong`.
        return "";
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(NewGame request) {
        int id = -1;
        try {
            id = AdventureService.instance().newGame(request.getUrl());
        } catch (Exception e) {
            // TODO: Make exception caught more specific.
            return Response
                    .status(Status.BAD_REQUEST)
                    .entity(new Error(e.getMessage()))
                    .build();
        }

        return getGame(id);
    }

    @GET
    @Path("instance/{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGame(@PathParam("id") int id) {
        // TODO: You might need to change this function if your game engine
        // has a different name or different methods.
        Adventure game = AdventureService.instance().getGame(id);
        if (game == null) {
            return instanceNotFound(id);
        }

//        GameStatus response = new GameStatus(id, game.getCurrentRoom(), game.isOver());
        GameStatus response = new GameStatus(id, Adventure.getCurrentRoom(), Adventure.isOver());
        return Response.ok(response).build();
    }

    @DELETE
    @Path("instance/{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroyGame(@PathParam("id") int id) {
        if (!AdventureService.instance().destroyGame(id)) {
            return instanceNotFound(id);
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("instance/{id: \\d+}/item/{item}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMarker(@PathParam("id") int id, @PathParam("item") String item) {
        if (!AdventureService.instance().removeItem(id, item)) {
            return instanceNotFound(id);
        }

        return getGame(id);
    }

    @PATCH
    @Path("instance/{id: \\d+}/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMarkers(@PathParam("id") int id, AddItems request) {
        List<String> items = request.getItems();
        if (!AdventureService.instance().addItems(id, items)) {
            return instanceNotFound(id);
        }

        return getGame(id);
    }

    @POST
    @Path("instance/{id: \\d+}/go")
    @Produces(MediaType.APPLICATION_JSON)
    public Response go(@PathParam("id") int id, Go request) throws Exception {
        // Set up output stream.
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream logger = new PrintStream(baos, true, "UTF-8");

        final String direction = request.getDirection();

        // Try to move in the specified direction.
        Adventure game = AdventureService.instance().goInDirection(id, direction, logger);
        String logOutput = new String(baos.toByteArray(), StandardCharsets.UTF_8);

        if (game != null) {
            return getGame(id);
        }

        if (logOutput.isEmpty()) {
            return instanceNotFound(id);
        }

        return Response
                .status(Status.BAD_REQUEST)
                .entity(new Error(logOutput))
                .build();
    }
}